class AddIndexToUser < ActiveRecord::Migration
  def self.up
    add_index :users, [:id, :token], unique: true
  end

  def self.down
    remove_index :users, [:id, :token]
  end
end
