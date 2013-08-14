class AddUserNumToRoom < ActiveRecord::Migration
  def self.up
    change_table :rooms do |t|
      t.integer :num_user
    end
  end

  def self.down
    change_table :rooms do |t|
      t.remove :num_user
    end
  end
end
