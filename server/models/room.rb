class Room < ActiveRecord::Base
  has_many :users
  has_many :missions
  has_many :locations
  has_many :hit_locations
  has_one :owner, :class_name => "User"

  before_save :count_member

  def started?
    self.termination_time == nil
  end

  def count_member
    self.num_user = self.members.size
  end

  def member_hash
    self.users.inject({}){|r, u|
      r[u.id] = u
      r
    }
  end
end
