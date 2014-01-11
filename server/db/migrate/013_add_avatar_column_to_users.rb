class AddAvatarColumnToUsers < ActiveRecord::Migration
  def self.up
    change_table :users do |t|
      t.string :avatar
    end
  end

  def self.down
    change_table :users do |t|
      t.remove :avatar
    end
  end
end
